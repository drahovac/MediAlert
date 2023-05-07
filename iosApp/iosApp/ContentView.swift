import SwiftUI
import KMMViewModelSwiftUI
import shared

struct ContentView: View {
    @StateViewModel var viewModel: CreateMedicineViewModel = CreateMedicineViewModel(medicineRepository: CreateMedicineViewModelHelper().medicineRepository)
	let greet = Greeting().greet()
    
    let titleResource = MR.strings().create_medicine_title
    

	var body: some View {
        VStack(alignment: .leading) {
            Text(
                LocalizedStringKey(titleResource.resourceId),
                bundle: titleResource.bundle
            )
            .font(.title)
            .padding(.bottom,16)
            let labelResource = MR.strings().create_medicine_name

                Text(LocalizedStringKey(labelResource.resourceId),
                     bundle: labelResource.bundle)
            TextField(MR.strings().create_medicine_enter_name.desc().localized(),
                      text: Binding<String>(
                    get: {String(viewModel.stateValue.name.value ?? "")},
                    set: {viewModel.updateName(name:  $0)}))
                .textFieldStyle(.roundedBorder)
            
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
        .padding(16)
       
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
