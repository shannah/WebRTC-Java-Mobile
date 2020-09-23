import Foundation


class PluginUtils {
	class func randomInt(_ min: Int, max: Int) -> Int {
		return Int(arc4random_uniform(UInt32(max - min))) + min
	}
    
    class func debug(_ format: String, _ args: CVarArg...) {
        if (!iosrtcPlugin.DEBUG) {
            return;
        }
        NSLog(format, args);
    }
}
