import mainModule from './bab.store.main.module';

import app from './component/app';
import login from './component/login';
import home from './component/home';

const componentMap = new Map([

    ['storeManage', require('./component/store_manage/store_manage')],
    ['storeFilterBar', require('./component/store_manage/store_filter_bar')],
    ['gridStoreManage', require('./component/store_manage/grid_store_manage')],
    ['searchCriteriaCommon', require('./component/store_manage/search_criteria_common')], 
    ['inputSearchStores', require('./component/store_manage/input_search_stores')], 
    ['timeSelector',require('./component/store_manage/time_selector')],
    ['temp', require('./component/temp')],
]);

let appComponent = app(),
    loginComponent = login(),
    homeComponent = home();

mainModule.value('$routerRootComponent', 'app');

console.debug('app component registered.');

mainModule.component('app', appComponent)
    .component('login', loginComponent)
    .component('home', homeComponent);

componentMap.forEach((value, key, map) => {
    if (typeof value.default === 'function') {
        mainModule.component(key, value.default());
    } else {
        mainModule.component(key, value.default);
    }
    
});

mainModule.config(['$locationProvider', function ($locationProvider) {
    // $locationProvider.html5Mode(true);
}]);

export default mainModule;