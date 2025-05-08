import Foundation
import UIKit
import NitroModules

class HybridTelephoto : HybridTelephotoSpec {
    
    // UIView
    var view: UIView = UIView()

    var source: String = ""
    
    var contentDescription: String?
    
    var alignment: Alignment?
    
    var contentScale: ContentScale?
    
    var minZoomFactor: Double?
    
    var maxZoomFactor: Double?
    
    var onPress: ((Offset) -> Void)?
    
    var onLongPress: ((Offset) -> Void)?
    
    var onZoomFractionChanged: ((Double?) -> Void)?
    
    func zoomTo(factor: Double, centroid: Offset) throws -> Promise<Void> {
        return Promise.async {}
    }
    
    func zoomBy(factor: Double, centroid: Offset) throws -> Promise<Void> {
        return Promise.async {}
    }
    
    func resetZoom() throws -> Promise<Void> {
        return Promise.async {}
    }
}
