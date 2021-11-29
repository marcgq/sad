//
//  ContentView.swift
//  Shared
//
//  Created by Marc on 7/11/21.
//

import SwiftUI

struct ContentView: View {
    @StateObject private var userInfo = UserInfo() // 1
    
    var body: some View {
        NavigationView {
                SettingsScreen()
        }
        .environmentObject(userInfo) // 2
        .navigationViewStyle(StackNavigationViewStyle()) // 3
        
        }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
