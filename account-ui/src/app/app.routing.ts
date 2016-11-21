import {Routes, RouterModule} from "@angular/router";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {ChangePasswordComponent} from "./components/profile/change.password";
import {ProfileFormComponent} from "./components/profile/change.profile";
import {UsersListComponent} from './components/users/users.list'
export const appRoutes: Routes = [
    {
        path: '',
        redirectTo: '/dashboard',
        pathMatch: 'full'
    },

    {
        path: 'user/profile',
        component: ProfileComponent
    }, {
        path: 'user/change_password',
        component: ChangePasswordComponent
    }, {
        path: 'user/profile/edit',
        component: ProfileFormComponent
    }, {
        path: 'users',
        component: UsersListComponent
    }, {
        path: 'dashboard',
        component: DashboardComponent
    },
];

export const routing = RouterModule.forRoot(appRoutes, {useHash: true});

export const asyncRoutes: AsyncRoutes = {};

export const prefetchRouteCallbacks: Array<IdleCallbacks> = [];
