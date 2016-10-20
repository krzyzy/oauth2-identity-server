import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'maxStringLength'
})
export class MaxStringLength implements PipeTransform {

  transform(initialString: string, maxLength: number): string {
    if(initialString.length <= maxLength) {
      return initialString;
    }
    return initialString.substr(0, maxLength - 3) + '...';
  }

}
