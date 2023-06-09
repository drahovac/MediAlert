import SwiftUI
import KMMViewModelSwiftUI
import shared

struct ContentView: View {
    @StateViewModel var viewModel: CreateMedicineViewModel = CreateMedicineViewModel(medicineRepository: CreateMedicineViewModelHelper().medicineRepository)
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
            StringInput(
                label: MR.strings().create_medicine_name.desc().localized(),
                hint:MR.strings().create_medicine_enter_name.desc().localized(),
                binding: Binding<String>(
                    get: {String(viewModel.stateValue.name.value ?? "")},
                    set: {viewModel.updateName(name:  $0)})
            )
            IntInput(
                label: MR.strings().create_medicine_blister_pack_count.desc().localized(),
                hint: MR.strings().create_medicine_blister_pack_count_enter.desc().localized(),
                binding: Binding<String>(
                    get: {String(viewModel.stateValue.blisterPackCount.value?.description ?? "")},
                    set: {viewModel.updateBlisterPacksCount(count:  $0)}))
            Toggle(MR.strings().create_medicine_identical.desc().localized(),
                   isOn: Binding<Bool>(
                    get: { viewModel.stateValue.areAllPacksIdentical },
                    set: {_ in viewModel.updateAllPacksIdentical()}))
        }
        .frame(
            maxWidth: .infinity,
            maxHeight: .infinity,
            alignment: .topLeading)
        .padding(16)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
