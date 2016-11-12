package com.blundell.hangovercures;

public interface Log {
    void d(String message);

    void e(String message);

    void e(String message, Throwable e);
}
