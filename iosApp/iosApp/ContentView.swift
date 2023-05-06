import SwiftUI
import shared

struct ContentView: View {
    let viewModel: CreateMedicineObservableViewModel = CreateMedicineObservableViewModel()    
    
	let greet = Greeting().greet()

	var body: some View {
        TextField("Medicine name", text: Binding<String>(
                        get: {String(viewModel.state.name.value ?? "")},
                        set: {viewModel.viewModel.updateName(name:  $0)}))
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
