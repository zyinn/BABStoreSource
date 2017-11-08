import angular from 'angular';

import 'bab.common';
import 'spin';
import 'file-saver';
import 'bab.common.css';

import './style/temp.less';
import './style/store_filter_bar.less';
import './style/panel_base_dialog.less';

import './style/grid_excel_import.less';
import './style/input_search_stores.less';
import './style/search_criteria_common.less';
import './style/time_selector.less';
export default angular.module('bab.store', [

    'bab.common',
    'ui.grid',
    'ui.grid.edit',
    'ui.grid.infiniteScroll',
    'ui.grid.cellNav',
    'ui.grid.pinning'
]);