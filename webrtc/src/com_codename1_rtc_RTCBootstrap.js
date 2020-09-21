

if (typeof String.prototype.toCamel !== 'function') {
    String.prototype.toCamel = function(){
      return this.replace(/[-_]([a-z])/g, function (g) { return g[1].toUpperCase(); })
    };
  }

function uuid(){
    var dt = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (dt + Math.random()*16)%16 | 0;
        dt = Math.floor(dt/16);
        return (c=='x' ? r :(r&0x3|0x8)).toString(16);
    });
    return uuid;
}




var f =function() {
    window.cn1 = {
        wrapRTCRtpReceiver : wrapRTCRtpReceiver,
        wrapRTCRtpTransceiver : wrapRTCRtpTransceiver,
        wrapRTCRtpSender : wrapRTCRtpSender,
        wrapRTCRtpContributingSource : wrapRTCRtpContributingSource,
        wrapRTCDtlsTransport : wrapRTCDtlsTransport,
        wrapMediaStream : wrapMediaStream,
        wrapMediaStreamTrack : wrapMediaStreamTrack,
        wrapMediaTrackSettings : wrapMediaTrackSettings,
        wrapMediaTrackConstraints : wrapMediaTrackConstraints,
        wrapMediaTrackCapabilities : wrapMediaTrackCapabilities,
        wrapRTCRtpSendParameters : wrapRTCRtpSendParameters,
        wrapRTCSessionDescription : wrapRTCSessionDescription

    };

    function wrapRTCSessionDescription(desc) {
        return desc;
    }

    function wrapRTCRtpSendParameters(params) {
        return params;
    }



    function addEventListener(o, type_) {
        var type = Array.isArray(type_) ? type_[0] : type_;


        o.addEventListener(type, function(e) {
            if (window.cn1Callback) {
                var data = {
                    target: registry.id(o),
                    type: type
                };
                if (Array.isArray(type_)) {
                    for (key in type_[1]) {
                        data[key] = type_[1][key](o, e);
                    }
                }
                console.log("IN EVENT for ",type, e);
                window.cn1Callback(data);
            }
        });
    }



    function wrapRTCRtpReceiver(receiver) {
        //https://developer.mozilla.org/en-US/docs/Web/API/RTCRtpReceiver
        if (!receiver) return null;
        var out = {};
        registry.retain(receiver);
        out.refId = registry.id(receiver);
        if (receiver.track) {
            out.track = wrapMediaStreamTrack(receiver.track);
        }
        out.contributingSources = [];
        if (receiver.getContributingSources) {
            receiver.getContributingSources().forEach(function(source, index, array) {
                out.contributingSources.push(wrapRTCRtpContributingSource(source));
            });
        }
        try {
            out.parameters = receiver.getParameters();
        } catch (e) {
            out.parameters = {};
        }


        console.log("wrapRTCRtpReceiver out", out);
        return out;
    }

    function wrapRTCRtpTransceiver(transceiver) {
        if (!transceiver) {
            return null;
        }
        console.log("In wrapRTCRtpTranceiver", transceiver);
        var out = {};
        registry.retain(transceiver);
        out.refId = registry.id(transceiver);
        out.currentDirection = transceiver.currentDirection;
        out.direction = transceiver.direction;
        out.mid = transceiver.mid;
        out.stopped = transceiver.stopped;
        out.receiver = wrapRTCRtpReceiver(transceiver.receiver);
        out.sender = wrapRTCRtpSender(transceiver.sender);
        console.log("wrapRTCRtpTransceiver out", out);
        return out;
    }

    function wrapRTCRtpSender(sender) {
        if (!sender) return null;
        var out = {};
        console.log("in wrapRTCRtpSender", sender);
        registry.retain(sender);
        out.refId = registry.id(sender);
        out.dtmf = sender.dtmf;
        out.rtcpTransport = sender.rtcpTransport;
        out.track = wrapMediaStreamTrack(sender.track);
        console.log("wrapRTCRtpSender out", out);
        return out;
    }

    function wrapRTCRtpContributingSource(source) {
        if (!source) return null;
        var out = {};
        var props = ['audioLevel', 'rtpTimestamp', 'source', 'timestamp'];
        props.forEach(function(prop, index, array){
            out[prop] = source[prop];
        });
        return out;
    }

    function wrapRTCDtlsTransport(transport) {
        // Not supported in Safari yet so we'll skip for now
        //https://developer.mozilla.org/en-US/docs/Web/API/RTCDtlsTransport
        if (!transport) return null;
        var out = {};

        return out;

    }

    function wrapMediaStream(stream) {
        if (!stream) {
            return null;
        }
        console.log("in wrapMediaStream ", stream);
        var out = {};
        registry.retain(stream);
        out.refId = registry.id(stream);
        var props = ['active', 'ended', 'id'];
        props.forEach(function(prop, index, array) {
            out[prop] = stream[prop];
        });
        out.tracks = [];
        stream.getTracks().forEach(function(track, index, array) {
            out.tracks.push(wrapMediaStreamTrack(track));
        });
        console.log("wrapMediaStream out ", out);
        return out;
    }

    function wrapMediaStreamTrack(track) {
        if (!track) {
            return null;
        }
        console.log("in wrapMediaStreamTrack", track);
        var out = {};
        registry.retain(track);
        out.refId = registry.id(track);
        out.id = track.id;
        out.enabled = track.enabled || false;
        out.isolated = track.isolated;
        out.kind = track.kind;
        out.label = track.label || '';
        out.muted = track.muted || false;
        out.readonly = track.readonly || false;
        out.readyState = track.readyState;
        out.remote = track.remote || false;
        out.settings = wrapMediaTrackSettings(track.kind, track.getSettings());
        try {
            out.constraints = wrapMediaTrackConstraints(track.kind, track.getConstraints());
        } catch (e) {
            out.constraints = {};
        }
        out.capabilities = wrapMediaTrackCapabilities(track.kind, track.getCapabilities());
        console.log("wrapMediaStreamTrack out ", out);
        return out;
    }

    function wrapMediaTrackSettings(kind, settings) {
        if (!settings) return null;
        var out = {};
        var props = ['deviceId', 'groupId'];
        if (kind == 'audio') {
            props = props.concat(['autoGainControl', 'channelCount', 'echoCancellation', 'latency', 'noiseSuppression', 'sampleRate', 'sampleSize', 'volume']);
        } else if (kind == 'video' ) {
            props = props.concat(['aspectRatio', 'facingMode', 'frameRate', 'height', 'width', 'resizeMode']);
        } else if (kind == 'image') {
            props = props.concat(['whiteBalanceMode', 'exposureMode', 'focusMode', 'pointsOfInterest', 'exposureCompensation', 'colorTemperature', 'iso', 'brightness', 'contrast', 'saturation', 'sharpness', 'focusDistance', 'zoom', 'torch']);
        } else if (kind == 'screen') {
            props = props.concat(['curor', 'displaySurface', 'logicalSurface']);
        }
        props.forEach(function(prop, index, array) {
            out[prop] = settings[prop];
        });

        return out;

    }

    function wrapMediaTrackConstraints(kind, constraints) {
        if (!constraints) return null;
        var out = {};
        var props = ['deviceId', 'groupId'];
        if (kind == 'audio') {
            props = props.concat(['autoGainControl', 'channelCount', 'echoCancellation', 'latency', 'noiseSuppression', 'sampleRate', 'sampleSize', 'volume']);
        } else if (kind == 'video' ) {
            props = props.concat(['aspectRatio', 'facingMode', 'frameRate', 'height', 'width', 'resizeMode']);
        } else if (kind == 'image') {
            props = props.concat(['whiteBalanceMode', 'exposureMode', 'focusMode', 'pointsOfInterest', 'exposureCompensation', 'colorTemperature', 'iso', 'brightness', 'contrast', 'saturation', 'sharpness', 'focusDistance', 'zoom', 'torch']);
        } else if (kind == 'screen') {
            props = props.concat(['curor', 'displaySurface', 'logicalSurface']);
        }
        props.forEach(function(prop, index, array) {
            out[prop] = constraints[prop];
        });
        return out;
    }

    function wrapRTCIceCandidate(candidate) {
        if (!candidate) {
            return null;
        }
        var out = {};
        ['candidate', 'component', 'foundation', 'ip', 'port', 'priority', 'protocol', 'relatedAddress', 'relatedPort', 'sdpMid',
        'sdpMLineIndex', 'tcpType', 'type', 'usernameFragment'].forEach(function(prop, index, array) {
            out[prop] = candidate[prop];
        });
        return out;

    }

    function wrapRTCDataChannel(channel) {
        if (!channel) {
            return null;
        }
        registry.retain(channel);
        return {
            refId : registry.id(channel),
            id : channel.id,
            binaryType : channel.binaryType,
            bufferedAmount : channel.bufferedAmount,
            bufferedAmountLowThreshold : channel.bufferedAmountLowThreshold,
            label : channel.label,
            maxPacketLifeTime : channel.maxPacketLifeTime,
            maxRetransmits : channel.maxRetransmits,
            negotiated : channel.negotiated,
            ordered : channel.ordered,
            protocol : channel.protocol,
            readyState : channel.readyState,
        };
    }

    function wrapMessagePort(port) {
        if (!port) {
            return null;
        }
        registry.retain(port);
        var out = {
            refId : registry.id(channel)
        }
        return out;
    }

    function wrapMediaTrackCapabilities(kind, capabilities) {
        if (!capabilities) return null;
        var out = {};
        var props = ['deviceId', 'groupId'];
        if (kind == 'audio') {
            props = props.concat(['autoGainControl', 'channelCount', 'echoCancellation', 'latency', 'noiseSuppression', 'sampleRate', 'sampleSize', 'volume']);
        } else if (kind == 'video' ) {
            props = props.concat(['aspectRatio', 'facingMode', 'frameRate', 'height', 'width', 'resizeMode']);
        } else if (kind == 'image') {
            props = props.concat(['whiteBalanceMode', 'exposureMode', 'focusMode', 'pointsOfInterest', 'exposureCompensation', 'colorTemperature', 'iso', 'brightness', 'contrast', 'saturation', 'sharpness', 'focusDistance', 'zoom', 'torch']);
        } else if (kind == 'screen') {
            props = props.concat(['curor', 'displaySurface', 'logicalSurface']);
        }
        props.forEach(function(prop, index, array) {
            out[prop] = capabilities[prop];
        });
        return out;
    }

    function installEventListeners(o) {
        console.log("INSTALLING EVENT LISTENERS FOR ", o);
        if (o instanceof MediaStream) {
            addEventListener(o, 'ended');
            var tracks = o.getTracks();
            var len = tracks.length;
            for (var i=0; i<len; i++) {
                installEventListeners(tracks[i]);
            }

            return;
        } else if (o instanceof HTMLMediaElement) {
            ['abort', 'canplay', 'canplaythrough',
                ['durationchange', {'duration' : function(el){return el.duration;}}],
                'emptied', 'ended',
                ['error', {'message' : function(el){return el.error.message;}, 'code' : function(el){return el.error.code;}}],
                'loadeddata', 'loadedmetadata', 'loadstart', 'pause', 'play', 'playing',
                'progress',
                ['ratechange', {'playbackRate' : function(el) {return el.playbackRate;}}],
                'seeked', 'seeking', 'stalled', 'suspend',
                ['timeupdate', {'currentTime' : function(el){return el.currentTime;}}],
                ['volumechange', {'volume' : function(el){return el.volume;}}],
                'waiting']
                    .forEach(function(type, index, array) {
                    addEventListener(o, type);


                });

            if (o instanceof HTMLVideoElement) {
                addEventListener(o,
                    ['resize',
                        {'videoWidth' : function(el){return el.videoWidth;},
                            'videoHeight': function(el){return el.videoHeight;}
                        }
                    ]
                );
            }
        } else if (o instanceof MediaStreamTrack) {
            ['ended', 'mute', 'unmute'].forEach(function(type, index, array){
                addEventListener(o, type);
            });
        } else if (o instanceof RTCPeerConnection) {
            console.log("INSTALLING EVENT LISTENERS FOR RTCPeerConnection", o);
            [['connectionstatechange', {connectionState:function(el){return el.connectionState;}}],
                ['datachannel', {
                    //https://developer.mozilla.org/en-US/docs/Web/API/RTCDataChannelEvent
                     refId : function(el, e) {
                        registry.retain(e);
                        return registry.id(e);
                    },
                    channel : function(el, e) {
                        return wrapRTCDataChannel(e.channel);
                    }
                }],
                ['icecandidate', {
                        //https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnectionIceEvent
                        candidate : function(el,e){
                            //console.log('getting candidate in icecandidate event', el, e);
                            return wrapRTCIceCandidate(e.candidate);
                        }

                }],
                ['icecandidateerror', {
                    errorCode : function(el,e){return e.errorCode;}
                }],
                ['iceconnectionstatechange', {
                        iceConnectionState : function(el,e) {return el.iceConnectionState;}
                }],
                ['icegatheringstatechange', {
                        iceGatheringState : function(el, e) {return el.iceGatheringState;}
                }], 'isolationchange', 'identityresult',
                'negotiationneeded',
                ['signalingstatechange', {
                        signalingState : function(el, e) { return el.signalingState;}
                }],
                ['track', {
                        // https://developer.mozilla.org/en-US/docs/Web/API/RTCTrackEvent
                        refId : function(el,e) {
                            registry.retain(e);
                            return registry.id(e);
                        },
                        receiver : function(el, e) {
                            return wrapRTCRtpReceiver(e.receiver);
                        },
                        streams : function(el, e) {
                            var out = [];
                            e.streams.forEach(function(stream, index, array){
                                out.push(wrapMediaStream(stream));
                            });
                            return out;
                        },
                        track : function(el, e) {
                            console.log("in track event", el, e);
                            return wrapMediaStreamTrack(e.track);
                        },
                        transceiver : function(el, e) {
                            console.log("Getting transceiver!!!!!!", el, e);
                            var t = e.transceiver;
                            if (!t.sender) {
                                t.sender = {
                                    track: e.track
                                };
                            }
                            return wrapRTCRtpTransceiver(t);
                        }


                }]].forEach(function(type, index, array) {
                    addEventListener(o, type);
                });

        } else if (o instanceof RTCDataChannel) {
            [['message', {
                refId : function(el, e) {
                    registry.retain(e);
                    return registry.id(e);
                },
                data : function(el, e) {
                    return e.data;
                },
                origin : function(el, e) {
                    return null;
                },
                ports : function(el, e) {
                    var ports = [];
                    e.ports.forEach(function(port) {
                       ports.push(wrapMessagePort(port));
                    });
                    return ports;
                }

            }], 'open', 'close'].forEach(function(type, index, array) {
                addEventListener(o, type);
            });
        } else if (o instanceof MessagePort) {
            ['message', 'messageerror'].forEach(function(type) {
                addEventListener(o, type);
            });
        }
    }

    function RTCRegistry() {
        var registry = {};

        this.get = function(id) {
            return registry[id];
        };

        this.retain = function (o, requestedUuid) {
            if (typeof o == 'string') {
                o = registry[o];
            }
            if (!o) {
                throw new Error("Cannot find reference");
            }
            if (!o._cn1) {
                Object.defineProperty(o, "_cn1", {
                    enumerable: false,
                    writable: true,
                    readable: true
                });
                o._cn1 = {};
                o._cn1.count = 0;
                o._cn1.id = requestedUuid || uuid();
                registry[o._cn1.id] = o;
                installEventListeners(o);

            }

            o._cn1.count++;

        };

        this.release = function(o) {
            var origO = o;
            if (typeof o == 'string') {
                o = registry[o];
            }
            if (!o) {
                throw new Error("Cannot find reference for "+origO);
            }
            if (o._cn1) {
                if (o._cn1.id) {
                    if (--o._cn1.count == 0) {
                        var id = o._cn1.id;
                        delete o._cn1;
                        delete registry[id];

                    }
                    return;
                }
            }
            throw new Error("Object "+o+" is not reference counted");

        };

        this.id = function(o) {

            if (o && o._cn1 && o._cn1.id) {
                return o._cn1.id;
            }
            return '';
        };
    }

    window.registry = new RTCRegistry();
};
f();
