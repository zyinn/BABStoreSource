import mainModule from './bab.store.main.module';
import './app/regService.js';
import './app/regDirective.js';

import './bab.store.component';

mainModule.config(['$mdDateLocaleProvider', ($mdDateLocaleProvider) => {
    "use strict";

    $mdDateLocaleProvider.months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一', '十二'];
    $mdDateLocaleProvider.shortMonths = $mdDateLocaleProvider.months;
    $mdDateLocaleProvider.days = ['日', '一', '二', '三', '四', '五', '六'];
    $mdDateLocaleProvider.shortDays = $mdDateLocaleProvider.days;

    $mdDateLocaleProvider.formatDate = function(date) {
        return date.format('yyyy-MM-dd');
    };
}]);




