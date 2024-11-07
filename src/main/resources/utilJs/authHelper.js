function authenticatedFetch(url, options = {}) {
    const token = localStorage.getItem("jwtToken");
    options.headers = {
        ...options.headers,
        'Authorization': token,
        'Content-Type': 'application/json'
    };
    return fetch(url, options);
}
