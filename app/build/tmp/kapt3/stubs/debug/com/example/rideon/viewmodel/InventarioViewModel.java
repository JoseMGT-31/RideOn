package com.example.rideon.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013J\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u0017J\"\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\r0\u001cJ\b\u0010\u001d\u001a\u00020\u0017H\u0002R+\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00048F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\n\u0010\u000b\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t\u00a8\u0006\u001e"}, d2 = {"Lcom/example/rideon/viewmodel/InventarioViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "<set-?>", "Lcom/example/rideon/viewmodel/InventarioForm;", "form", "getForm", "()Lcom/example/rideon/viewmodel/InventarioForm;", "setForm", "(Lcom/example/rideon/viewmodel/InventarioForm;)V", "form$delegate", "Landroidx/compose/runtime/MutableState;", "clearForm", "", "loadForEdit", "producto", "Lcom/example/rideon/model/ProductoUi;", "onChange", "field", "", "value", "setAbs", "v", "", "submit", "defaultImageRes", "", "onSaved", "Lkotlin/Function1;", "validate", "app_debug"})
public final class InventarioViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState form$delegate = null;
    
    public InventarioViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.rideon.viewmodel.InventarioForm getForm() {
        return null;
    }
    
    private final void setForm(com.example.rideon.viewmodel.InventarioForm p0) {
    }
    
    /**
     * Cargar datos para editar (si corresponde)
     */
    public final void loadForEdit(@org.jetbrains.annotations.NotNull()
    com.example.rideon.model.ProductoUi producto) {
    }
    
    public final void clearForm() {
    }
    
    public final void onChange(@org.jetbrains.annotations.NotNull()
    java.lang.String field, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void setAbs(boolean v) {
    }
    
    private final boolean validate() {
        return false;
    }
    
    public final void submit(int defaultImageRes, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.rideon.model.ProductoUi, kotlin.Unit> onSaved) {
    }
}