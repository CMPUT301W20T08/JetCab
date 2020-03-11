package com.example.jetcab;

public interface RequestResponse {

    public abstract void CompletedRequest();
    public abstract void CancelledRequest();
    public abstract void ResponseOpenRequest();
    public abstract void AcceptedRequest();
}
