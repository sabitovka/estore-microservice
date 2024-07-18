import { initializeToolbar } from './components/toolbar.js';
import { initializePage as viewDataPageInit} from './pages/viewdata.js';
import { initializePage as editDataPageInit } from './pages/editdata.js';
import { initializePage as indexPageInit } from './pages/indexpage.js';
import { initializePage as referencesPageInit } from './pages/references.js';

import { addQueryParamAndReload } from './common/utils.js';

document.addEventListener('DOMContentLoaded', async function() {
    initializeToolbar();

    const path = window.location.pathname || '';
    if (path === '/' || path.startsWith('/index.html')) {
        await indexPageInit();
    }

    if (path.includes('viewdata.html')) {
        await viewDataPageInit();
    }

    if (path.includes('editdata.html')) {
        await editDataPageInit();
    }

    if (path.includes('references.html')) {
        await referencesPageInit();
    }
});

window.utils = {
    addQueryParamAndReload,
}