export function createParamsString(params) {
    if (!params) return '';
    const str = Object.entries(params).map(([k, v]) => {
        return !!v ? `${k}=${v}` : '';
    })?.filter(Boolean)?.join("&");
    return !!str ? `?${str}` : ''
}

export function addQueryParamAndReload(paramName, paramValue) {
    const searchParams = new URLSearchParams(window.location.search);
    searchParams.set(paramName, paramValue);
    const newUrl = window.location.pathname + '?' + searchParams.toString();
    window.location.href = newUrl;
}