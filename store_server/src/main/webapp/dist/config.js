(function (angular) {

    angular.module('bab.common').constant('configConsts', {


        maxQuoteDays: 390,

        service_root: undefined,
    });

    angular.module('bab.store').constant('configConsts', {
        tag: 'config.release const',

        DATE_FORMAT: 'yyyy-MM-dd',      

        bab_init_login: '/bab_store/storeInit/login', 
        bab_get_token: '/bab_store/management/getDataToken',

        bab_store_init: '/bab_store/storeInit/initData',

        search_stores_by_param: '/bab_store/storeQuery/searchStoresByParam',
        update_godown_store: '/bab_store/management/updateGoDownStore',
        create_out_store: '/bab_store/management/outStore',
        cancel_store: '/bab_store/management/cancelStore',

        post_discount: '/bab_store/management/postDiscount',        

        //Excel导入接口
        parser_go_down_stores: '/bab_store/management/parserGoDownStores',

        create_go_down_stores: '/bab_store/management/createGoDownStore',

        //Excel导出
        excel_export: '/bab_store/management/excelExports',

        search_stores_by_code: '/bab_store/management/getAndCheckBillInfo'


    });
})(window.angular)