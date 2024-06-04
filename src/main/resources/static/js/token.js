
const token = searchParam('token')

if ( token ){
    localStorage.setItem("access_token", token )
}

function searchParam(ke){
    return new URLSearchParams(location.search).get(key);
}

