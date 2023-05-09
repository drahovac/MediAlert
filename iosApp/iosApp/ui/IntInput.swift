//
//  IntInput.swift
//  iosApp
//
//  Created by Václav Drahorád on 09.05.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct IntInput : View {
    
    var label: String
    var hint: String
    var binding: Binding<String>
    
    var body: some View {
    
        Text(label)
        TextField(hint, text: binding)
            .keyboardType(.numberPad)
            .textFieldStyle(.roundedBorder)
    }
}

struct IntInput_Previews: PreviewProvider {
    
    static var previews: some View {
        VStack {
            IntInput(
                label: "Label",
                hint: "Hint",
                binding: Binding<String>(
                    get: {"Value"},
                    set: {_ in }
                )
            )
        }
    }
}
