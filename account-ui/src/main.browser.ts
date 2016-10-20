import { platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import { decorateComponentRef } from './platform/environment';

import {AppModule } from "./app/app.module";

require("font-awesome-webpack");

export function main(initialHmrState?: any): Promise<any> {

  return platformBrowserDynamic()
      .bootstrapModule(AppModule)
      .then(decorateComponentRef)
      .catch(err => console.error(err));

}

if ('development' === ENV && HMR === true) {
  // activate hot module reload
  let ngHmr = require('angular2-hmr');
  ngHmr.hotModuleReplacement(main, module);
} else {
  // bootstrap when document is ready
  document.addEventListener('DOMContentLoaded', () => main());
}
