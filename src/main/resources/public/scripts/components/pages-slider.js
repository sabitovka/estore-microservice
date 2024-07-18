export class PagesSlider {
    constructor() {
        this.currentPage = +this.getCurrentPage();
        window.PagesSlider = this;
    }

    getCurrentPage() {
        const urlParams = new URLSearchParams(window.location.search);
        const page = urlParams.get('page');
        return page || 0;
    }

    nextPage() {
        window.utils.addQueryParamAndReload('page', ++window.PagesSlider.currentPage);
    }

    prevPage() {
        if (window.PagesSlider.currentPage == 0) return
        window.utils.addQueryParamAndReload('page', --window.PagesSlider.currentPage);
    }

    render() {
        const sliderHtml = `
            <div class="btn-group" role="group">
                <button type="button" class="btn btn-secondary" onClick="window.PagesSlider.prevPage()">&lt;</button>
                <button type="button" class="btn btn-secondary" disabled>${this.currentPage + 1}</button>
                <button type="button" class="btn btn-secondary" onClick="window.PagesSlider.nextPage()">&gt;</button>
            </div>
        `

        const div = document.createElement('div')
        div.classList.add('d-flex', 'justify-content-end', 'py-4')
        div.insertAdjacentHTML('afterbegin', sliderHtml);
        return div;
    }
}