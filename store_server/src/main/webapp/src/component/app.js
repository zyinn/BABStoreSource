const initView = Symbol('initView');

class appCtrl {
    constructor($scope, $rootScope) {
        this.theme = this.theme || 'default';

        $rootScope.$on('gridRefresh', () => {
            $scope.$broadcast("gridRefresh");
        });
    };

    [initView]() {
        console.debug('appCtrl initView');
    };
};

let app = () => {
    return {
        template: require('./template/app.html'),
        // controller: 'appCtrl'        
        bindings: {
            theme: "@"
        },

        controller: ['$scope', '$rootScope', appCtrl],

        $routeConfig: [
            { path: '/login', name: 'Login', component: 'login', useAsDefault: true },
            { path: '/home/...', name: 'Home', component: 'home' },
            { path: '**', redirectTo: ['/Login'] }
        ]
    }
};

export default app;