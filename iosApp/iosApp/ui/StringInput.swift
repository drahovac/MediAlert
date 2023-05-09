//
//  StringInput.swift
//  iosApp
//
//  Created by Václav Drahorád on 08.05.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct StringInput : View {
    
    var label: String
    var hint: String
    var binding: Binding<String>
    
    var body: some View {
    
        Text(label)
        TextField(hint, text: binding)
            .textFieldStyle(.roundedBorder)
    }
}

struct StringInput_Previews: PreviewProvider {
    
    static var previews: some View {
        VStack {
            StringInput(
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
