package com.cartop.android.core.api;

import com.cartop.android.core.models.Program;
import com.cartop.android.core.models.ProgramsPage;
import com.cartop.android.core.models.Token;

public interface ApiDelegate {

     void onReceiveToken(int requestCode, Token token);

    void onReceiveProgramsPage(int requestCode, ProgramsPage programsPage);

    void onReceiveProgram(int requestCode, Program program);

    void onFailure(int requestCode, Throwable throwable);

    //region SimpleApiDelegate
    class SimpleApiDelegate implements ApiDelegate {
        @Override
        public void onReceiveToken(int requestCode, Token token) {

        }

        @Override
        public void onReceiveProgramsPage(int requestCode, ProgramsPage programsPage) {

        }

        @Override
        public void onReceiveProgram(int requestCode, Program program) {

        }

        @Override
        public void onFailure(int requestCode, Throwable throwable) {

        }
    }
    //endregion
}
