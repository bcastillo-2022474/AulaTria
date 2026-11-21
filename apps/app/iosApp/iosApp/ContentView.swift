import UIKit
import SwiftUI
import Shared

struct ComposeView: UIViewControllerRepresentable {
    // The simulator shares the host's network, so localhost is the iOS counterpart of the Android
    // emulator's 10.0.2.2. ApiBaseUrl comes from Config.xcconfig (override in Local.xcconfig for a real phone).
    private var apiBaseUrl: String {
        let value = Bundle.main.object(forInfoDictionaryKey: "ApiBaseUrl") as? String
        // A build setting left empty reaches Info.plist as an empty string, which is not a value.
        return (value?.isEmpty ?? true) ? "http://localhost:3000" : value!
    }

    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController(apiBaseUrl: apiBaseUrl)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}
