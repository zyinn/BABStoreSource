const initView = Symbol('initView');

class ctrl {

    constructor(userService, configConsts) {
        this.userService = userService;

        this.configConsts = configConsts;

        this[initView]();
    };

    [initView]() {
        console.debug('ctrl initView');

    };

    $routerOnActivate(currentInstruction, previousInstruction) {
       
    };
};

let temp = () => {
    console.debug('Create component temp');

    return {
        template: require('./template/temp.html'),
        bindings: {
            theme: '@mdTheme',
            $router: '<'
        },
        controller: ['userService', 'configConsts', ctrl]      
    };
};

export default temp;