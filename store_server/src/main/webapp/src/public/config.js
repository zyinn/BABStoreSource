(function (angular) {

    angular.module('bab.common').constant('configConsts', {

        showMainNavbar: true,

        maxQuoteDays: 390,

        // release 1.0.0
        // service_root: 'http://172.16.8.127:8510',
        // service_root: 'http://localhost:8888',

        // release 1.1.x
        service_root: 'http://172.16.66.81:8730',     
    });

    angular.module('bab.store').constant('configConsts', {
        tag: 'const',

        DATE_FORMAT: 'yyyy-MM-dd',

        service_root: 'http://172.16.66.81:8730',
        alternativeUser: [
            {
                displayName: "110956717@qm.com",
                userName: "110956717@qm.com",
                password: "123456",
                description: "BAB.QUOTE.SSR.MANAGEMENT"
            },
            {
                displayName: "windely03",
                userName: "windely03",
                password: "123456",
                description: "BAB.QUOTE.SSR.MANAGEMENT BAB.QUOTE.NPC.VIEW"
            },
            { displayName: "mt1667", userName: "mt1667", password: "123456", description: "无特殊权限" },
            {
                displayName: "roomtest185", userName: "roomtest185", password: "123456", default: true,
                description: "BAB.QUOTE.SSR.MANAGEMENT BAB.QUOTE.NPC.VIEW BAB.QUOTE.NPC.MANAGEMENT BAB.QUOTE.BATCHIMPORT"
            }],

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