import SwiftUI
import KMMViewModelSwiftUI
import shared

struct ContentView: View {
    @StateViewModel var viewModel: CreateMedicineObservableViewModel = CreateMedicineObservableViewModel(medicineRepository: CreateMedicineViewModelHelper().medicineRepository)
	let greet = Greeting().greet()

	var body: some View {
        TextField("Medicine name", text: Binding<String>(
                        get: {String(viewModel.stateValue.name.value ?? "")},
                        set: {viewModel.updateName(name:  $0)}))
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
