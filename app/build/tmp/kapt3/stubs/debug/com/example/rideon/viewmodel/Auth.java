package com.example.rideon.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B#\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\'0&J\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\'0&J\u0010\u0010)\u001a\u00020\'2\u0006\u0010*\u001a\u00020+H\u0002J\u0014\u0010,\u001a\u00020-2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020-0/J\u000e\u00100\u001a\u00020-2\u0006\u00101\u001a\u00020+J\u000e\u00102\u001a\u00020-2\u0006\u00101\u001a\u00020+J\u000e\u00103\u001a\u00020-2\u0006\u00101\u001a\u00020+J\u000e\u00104\u001a\u00020-2\u0006\u00101\u001a\u00020+J\u000e\u00105\u001a\u00020-2\u0006\u00101\u001a\u00020+J\u000e\u00106\u001a\u00020-2\u0006\u00101\u001a\u00020+J(\u00107\u001a\u00020-2\f\u00108\u001a\b\u0012\u0004\u0012\u00020-0/2\u0012\u00109\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020-0:J(\u0010;\u001a\u00020-2\f\u00108\u001a\b\u0012\u0004\u0012\u00020-0/2\u0012\u00109\u001a\u000e\u0012\u0004\u0012\u00020+\u0012\u0004\u0012\u00020-0:J\u000e\u0010<\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010+0&J\b\u0010=\u001a\u00020-H\u0002J\b\u0010>\u001a\u00020-H\u0002R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00100\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0018R\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00120\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u0014\u0010\u001f\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0014\u0010\"\u001a\u00020\b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010$\u00a8\u0006?"}, d2 = {"Lcom/example/rideon/viewmodel/Auth;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "repoOverride", "Lcom/example/rideon/repository/UserRepository;", "sessionOverride", "Lcom/example/rideon/viewmodel/SessionManagerType;", "(Landroid/app/Application;Lcom/example/rideon/repository/UserRepository;Lcom/example/rideon/viewmodel/SessionManagerType;)V", "_loginErrors", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/rideon/viewmodel/LoginErrors;", "_loginState", "Lcom/example/rideon/viewmodel/LoginUiState;", "_registerErrors", "Lcom/example/rideon/viewmodel/RegisterErrors;", "_registerState", "Lcom/example/rideon/viewmodel/RegisterUiState;", "_repoOverride", "_sessionOverride", "loginErrors", "Lkotlinx/coroutines/flow/StateFlow;", "getLoginErrors", "()Lkotlinx/coroutines/flow/StateFlow;", "loginState", "getLoginState", "registerErrors", "getRegisterErrors", "registerState", "getRegisterState", "repo", "getRepo", "()Lcom/example/rideon/repository/UserRepository;", "session", "getSession", "()Lcom/example/rideon/viewmodel/SessionManagerType;", "isAdminFlow", "Lkotlinx/coroutines/flow/Flow;", "", "isLoggedInFlow", "isValidEmail", "email", "", "logout", "", "onDone", "Lkotlin/Function0;", "onLoginEmailChange", "v", "onLoginPasswordChange", "onRegisterConfirmChange", "onRegisterEmailChange", "onRegisterNameChange", "onRegisterPasswordChange", "submitLogin", "onSuccess", "onFailure", "Lkotlin/Function1;", "submitRegister", "userRoleFlow", "validateLogin", "validateRegister", "app_debug"})
public final class Auth extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.Nullable()
    private com.example.rideon.repository.UserRepository _repoOverride;
    @org.jetbrains.annotations.Nullable()
    private com.example.rideon.viewmodel.SessionManagerType _sessionOverride;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.rideon.viewmodel.LoginUiState> _loginState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.LoginUiState> loginState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.rideon.viewmodel.LoginErrors> _loginErrors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.LoginErrors> loginErrors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.rideon.viewmodel.RegisterUiState> _registerState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.RegisterUiState> registerState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.rideon.viewmodel.RegisterErrors> _registerErrors = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.RegisterErrors> registerErrors = null;
    
    public Auth(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    public Auth(@org.jetbrains.annotations.NotNull()
    android.app.Application application, @org.jetbrains.annotations.Nullable()
    com.example.rideon.repository.UserRepository repoOverride, @org.jetbrains.annotations.Nullable()
    com.example.rideon.viewmodel.SessionManagerType sessionOverride) {
        super(null);
    }
    
    private final com.example.rideon.repository.UserRepository getRepo() {
        return null;
    }
    
    private final com.example.rideon.viewmodel.SessionManagerType getSession() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.LoginUiState> getLoginState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.LoginErrors> getLoginErrors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.RegisterUiState> getRegisterState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.rideon.viewmodel.RegisterErrors> getRegisterErrors() {
        return null;
    }
    
    public final void onLoginEmailChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onLoginPasswordChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    private final void validateLogin() {
    }
    
    public final void submitLogin(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onFailure) {
    }
    
    public final void onRegisterNameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onRegisterEmailChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onRegisterPasswordChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onRegisterConfirmChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    private final void validateRegister() {
    }
    
    public final void submitRegister(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onFailure) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isLoggedInFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isAdminFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> userRoleFlow() {
        return null;
    }
    
    public final void logout(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDone) {
    }
    
    private final boolean isValidEmail(java.lang.String email) {
        return false;
    }
}