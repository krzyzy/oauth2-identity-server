import {Component} from "@angular/core";

export interface ChangePasswordForm {
    password: string; // required, value must be equal to confirm password.
    confirmPassword: string; // required, value must be equal to password.
}

@Component({
    selector: 'user-change-password',
    templateUrl: 'change.password.html'
})
export class ChangePasswordComponent {

    constructor() {
        this.form = {
            password: ''
        };
    }

    save(model: ChangePasswordForm, isValid: boolean) {
        console.log(model);
    }
}
