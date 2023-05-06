//
//  CreateMedicineObservableViewModel.swift
//  iosApp
//
//  Created by Václav Drahorád on 04.05.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

class CreateMedicineObservableViewModel : ObservableObject {
    let viewModel : CreateMedicineViewModel = CreateMedicineViewModelHelper().viewModel
    @Published var state: CreateMedicineState = CreateMedicineState.companion.doInitCreateMedicineState()
    
    init(){
       state =  viewModel.state.value
    }
}
