package com.example.rideon.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u000e\u001a\u00020\u000fH\u00a6@\u00a2\u0006\u0002\u0010\u0010J&\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u00a6@\u00a2\u0006\u0002\u0010\u0015R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0005R\u0018\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0005R\u001a\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u0005R\u001a\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\u0005R\u001a\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u0005\u00a8\u0006\u0016"}, d2 = {"Lcom/example/rideon/viewmodel/SessionManagerType;", "", "isAdmin", "Lkotlinx/coroutines/flow/Flow;", "", "()Lkotlinx/coroutines/flow/Flow;", "isLoggedIn", "userEmail", "", "getUserEmail", "userName", "getUserName", "userRole", "getUserRole", "clearSession", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveSession", "name", "email", "role", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface SessionManagerType {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> isLoggedIn();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.String> getUserEmail();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.String> getUserName();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.String> getUserRole();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> isAdmin();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object saveSession(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String role, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}