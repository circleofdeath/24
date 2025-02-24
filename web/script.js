const injector = document.getElementById('template-store-injector');
const urlParams = new URLSearchParams(window.location.search);
const apiServer = 'http://localhost:8080/api';
let selected_element = null;

function updateQueryString(key, value) 
{   const url = new URL(window.location)
;   url.searchParams.set(key, value)
;   window.history.pushState({}, '', url)
;   urlParams.set(key, value)
;
}

function updatePage(id) 
{   updateQueryString('page', id)
;   const element = document.getElementById(`template-store-${id}`)

;   if(element == null) 
    {   updatePage('404')
    ;   return
    ;
    }

    if(selected_element != null) {
        document.getElementById(selected_element).innerHTML = injector.innerHTML;
    }

    injector.innerHTML = element.innerHTML
    selected_element = `template-store-${id}`;
    element.innerHTML = '';
;
}

if(!urlParams.has('page')) 
{   updatePage('index')
;
} 
else 
{   updatePage(urlParams.get('page'))
;
}

function getAuthFromLocalStore()
{   return {   
        password: localStorage.getItem('auth-password')
    ,   email: localStorage.getItem('auth-email')
    }
;
}

function signUpButtonLogic()
{   fetch(`${apiServer}/users/register`, 
        {   method: 'POST'
        ,   headers: 
            {   'Content-Type': 'application/json'
            }
        ,   body: JSON.stringify(
            {   username: document.getElementById('reg-username').value
            ,   email: document.getElementById('reg-email').value
            ,   password: document.getElementById('reg-paswd').value
            })
        })
        .then(() => 
        {   localStorage.setItem('auth-password', document.getElementById('reg-paswd').value)
        ;   localStorage.setItem('auth-email', document.getElementById('reg-email').value)
        ;   authFetch()?.then(r => 
            {   if(r)
                {   updatePage('dashboard')
                    authCheck()
                ;
                }
                else
                {
                    alert('Unknown error!')
                ;
                }
            })
        ;  
        })
;   
;
}

function logInButtonLogic()
{   localStorage.setItem('auth-password', document.getElementById('login-paswd').value)
;   localStorage.setItem('auth-email', document.getElementById('login-email').value)
;   authFetch()?.then(r =>
    {
        if(r)
        {   updatePage('dashboard')
            authCheck()
        }
        else
        {
            alert('Invalid credentials')
        }
    }
    )
;
}

function authFetch()
{   let auth = getAuthFromLocalStore()
;   if(auth.password != null && auth.email != null)
    {   return fetch(`${apiServer}/users/login`, 
        {   method: 'POST'
        ,   headers: 
            {   'Content-Type': 'application/json'
            }
        ,   body: JSON.stringify(auth)
        })
            .then(r => r.json())
    ;
    }
    return undefined;
;
}

function logOut()
{   localStorage.removeItem('auth-password')
;   localStorage.removeItem('auth-email')
;
}

function authCheck()
{   authFetch()?.then(r => 
    {   let auth = getAuthFromLocalStore()   
    ;   if(r)
        {   console.log('Logged in successfully')
        ;   fetch(`${apiServer}/users/usernameByEmail/${auth.email}`, {method: 'GET'})
                .then(r => r.text()).then(u => 
                {   document.querySelector('.header').innerHTML = `
                    <a class="header-a" href="?page=dashboard">${u}</a>
                    <a class="header-a" href="?page=index" onClick="logOut()">Log out</a>
                `
                ;
                }
            )
        ;   updatePage('dashboard')
        ;
        }
        else
        {   console.log('Failed to login: incorrect')
        ;
        }
    }
    )
    ;
}

authCheck()