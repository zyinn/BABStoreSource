const initView = Symbol('initView');

const THEME_NAME = "ssAvalonUi";

const dataDefine = {

};

class homeCtrl {

    constructor($scope, userService, configConsts) {
        this.theme = THEME_NAME;

        this.$scope = $scope;

        this.userService = userService;

        this.configConsts = configConsts;

        this[initView]();
    };

    [initView]() {
        console.debug('homeCtrl initView');  
    };

    // 登录验证 + 路由拦截
    $routerOnActivate(currentInstruction, previousInstruction) {    
        if (!this.userService.getUserInfo()) {
            this.$router.navigate(['Login']);
        }
    };
};

let home = () => {
    console.debug('Create component home');

    return {
        template: require('./template/home.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['$scope', 'userService', 'configConsts', homeCtrl],
        $routeConfig: [
            { path: '/store_manage', name: 'StoreManage', component: 'storeManage', data: { theme: THEME_NAME }, useAsDefault: true },

            { path: '/template', name: 'Template', component: 'temp' }
        ]
    }
};

export default home;