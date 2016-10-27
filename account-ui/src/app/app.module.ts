import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule, XHRBackend } from '@angular/http';
import { Ng2Bs3ModalModule } from 'ng2-bs3-modal/ng2-bs3-modal';

import {
    AppComponent,
    DashboardComponent,
    ProfileComponent,
    MaxStringLength,
    routing
} from './';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        routing,
        HttpModule,
        Ng2Bs3ModalModule
    ],
    declarations: [
        AppComponent,
        DashboardComponent,
        ProfileComponent,
        MaxStringLength,
    ],
    providers: [
    ],
    bootstrap: [AppComponent]
})
export class AppModule {

}
