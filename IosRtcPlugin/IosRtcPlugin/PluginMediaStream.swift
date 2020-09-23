import Foundation

class PluginMediaStream : NSObject {
    var pcId: Int?
	var rtcMediaStream: RTCMediaStream
	var id: String
	var audioTracks: [String : PluginMediaStreamTrack] = [:]
	var videoTracks: [String : PluginMediaStreamTrack] = [:]
	var eventListener: ((_ data: NSDictionary) -> Void)?
	var eventListenerForAddTrack: ((_ pluginMediaStreamTrack: PluginMediaStreamTrack) -> Void)?
	var eventListenerForRemoveTrack: ((_ id: String) -> Void)?

	/**
	 * Constructor for pc.onaddstream event and getUserMedia().
	 */
    init(rtcMediaStream: RTCMediaStream, pcId: Int?) {
		
        self.pcId = pcId
		self.rtcMediaStream = rtcMediaStream

		/// Handle possible duplicate remote streamId with janus or short duplicate name
		// See: https://github.com/cordova-rtc/cordova-plugin-iosrtc/issues/432
		if (rtcMediaStream.streamId.count < 36) {
			self.id = rtcMediaStream.streamId + "_" + UUID().uuidString;
		} else {
			self.id = rtcMediaStream.streamId;
		}

        if (pcId != nil) {
            self.id += ":" + String(pcId!)
        }
        PluginUtils.debug("PluginMediaStream#init(): %@", self.id)
		for track: RTCMediaStreamTrack in (self.rtcMediaStream.audioTracks as Array<RTCMediaStreamTrack>) {
            let pluginMediaStreamTrack = PluginMediaStreamTrack(rtcMediaStreamTrack: track, pcId: pcId)

			pluginMediaStreamTrack.run()
			self.audioTracks[pluginMediaStreamTrack.id] = pluginMediaStreamTrack
		}

		for track: RTCMediaStreamTrack in (self.rtcMediaStream.videoTracks as Array<RTCMediaStreamTrack>) {
            let pluginMediaStreamTrack = PluginMediaStreamTrack(rtcMediaStreamTrack: track, pcId: pcId)

			pluginMediaStreamTrack.run()
			self.videoTracks[pluginMediaStreamTrack.id] = pluginMediaStreamTrack
		}
	}

	deinit {
		PluginUtils.debug("PluginMediaStream#deinit()")
		for (id, _) in audioTracks {
			if(self.eventListenerForRemoveTrack != nil) {
				self.eventListenerForRemoveTrack!(id)
			}
		}
		for (id, _) in videoTracks {
			if(self.eventListenerForRemoveTrack != nil) {
				self.eventListenerForRemoveTrack!(id)
			}
		}
	}

    
    func publicId() -> String {
        if (self.id.contains(":")) {
            return String(self.id[...self.id.index(of: ":")!])
            
        } else {
            return self.id
        }
    }
    
	func run() {
		PluginUtils.debug("PluginMediaStream#run()")
	}

	func getJSON() -> NSDictionary {
		let json: NSMutableDictionary = [
			"id": self.id,
			"audioTracks": NSMutableDictionary(),
			"videoTracks": NSMutableDictionary()
		]

		for (id, pluginMediaStreamTrack) in self.audioTracks {
			(json["audioTracks"] as! NSMutableDictionary)[id] = pluginMediaStreamTrack.getJSON()
		}

		for (id, pluginMediaStreamTrack) in self.videoTracks {
			(json["videoTracks"] as! NSMutableDictionary)[id] = pluginMediaStreamTrack.getJSON()
		}

		return json as NSDictionary
	}

	func setListener(
		_ eventListener: @escaping (_ data: NSDictionary) -> Void,
		eventListenerForAddTrack: ((_ pluginMediaStreamTrack: PluginMediaStreamTrack) -> Void)?,
		eventListenerForRemoveTrack: ((_ id: String) -> Void)?
	) {
		PluginUtils.debug("PluginMediaStream#setListener()")

		self.eventListener = eventListener
		self.eventListenerForAddTrack = eventListenerForAddTrack
		self.eventListenerForRemoveTrack = eventListenerForRemoveTrack
	}

	func addTrack(_ pluginMediaStreamTrack: PluginMediaStreamTrack) -> Bool {
		PluginUtils.debug("PluginMediaStream#addTrack()")

		if pluginMediaStreamTrack.kind == "audio" {
			self.rtcMediaStream.addAudioTrack(pluginMediaStreamTrack.rtcMediaStreamTrack as! RTCAudioTrack)
			PluginUtils.debug("PluginMediaStream#addTrack() | audio track added")
			self.audioTracks[pluginMediaStreamTrack.id] = pluginMediaStreamTrack
		} else if pluginMediaStreamTrack.kind == "video" {
			self.rtcMediaStream.addVideoTrack(pluginMediaStreamTrack.rtcMediaStreamTrack as! RTCVideoTrack)
			PluginUtils.debug("PluginMediaStream#addTrack() | video track added")
			self.videoTracks[pluginMediaStreamTrack.id] = pluginMediaStreamTrack
		} else {
			return false
		}

		onAddTrack(pluginMediaStreamTrack)
		return true
	}

	func removeTrack(_ pluginMediaStreamTrack: PluginMediaStreamTrack) -> Bool {
		PluginUtils.debug("PluginMediaStream#removeTrack()")

		if pluginMediaStreamTrack.kind == "audio" {
			self.audioTracks[pluginMediaStreamTrack.id] = nil
			self.rtcMediaStream.removeAudioTrack(pluginMediaStreamTrack.rtcMediaStreamTrack as! RTCAudioTrack)
			PluginUtils.debug("PluginMediaStream#removeTrack() | audio track removed")
		} else if pluginMediaStreamTrack.kind == "video" {
			self.videoTracks[pluginMediaStreamTrack.id] = nil
			self.rtcMediaStream.removeVideoTrack(pluginMediaStreamTrack.rtcMediaStreamTrack as! RTCVideoTrack)
			PluginUtils.debug("PluginMediaStream#removeTrack() | video track removed")
		} else {
			return false
		}

		onRemoveTrack(pluginMediaStreamTrack)
		return true
	}

	func onAddTrack(_ track: PluginMediaStreamTrack) {
		PluginUtils.debug("PluginMediaStream | OnAddTrack [label:%@]", String(track.id))

		track.run()

		if self.eventListener != nil {
			self.eventListenerForAddTrack!(track)

			self.eventListener!([
				"type": "addtrack",
				"track": track.getJSON()
			])
		}
	}

	func onRemoveTrack(_ track: PluginMediaStreamTrack) {
		PluginUtils.debug("PluginMediaStream | OnRemoveTrack [label:%@]", String(track.id))

		// It may happen that track was removed due to user action (removeTrack()).
		if self.audioTracks[track.id] != nil {
			self.audioTracks[track.id] = nil
		} else if self.videoTracks[track.id] != nil {
			self.videoTracks[track.id] = nil
		} else {
			return
		}

		if self.eventListener != nil {
			self.eventListenerForRemoveTrack!(track.id)

			self.eventListener!([
				"type": "removetrack",
				"track": [
					"id": track.id,
					"kind": track.kind
				]
			])
		}
	}
}
