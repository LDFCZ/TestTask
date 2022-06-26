import React, { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";

import './style.css'
import "bootstrap/dist/css/bootstrap.min.css";

import AuthService from "./security/services/authService";

import Login from "./security/components/LoginComponent";
import Register from "./security/components/RegisterComponent";
import BoardAdmin from "./security/components/BoardAdminComponent";
import EventBus from "./common/EventBus";
import BannerComponent from "./components/BannerComponent";
import CategoryComponent from "./components/CategoryComponent";
import BidComponent from "./components/BidComponent";
import ProfileComponent from "./security/components/ProfileComponent";
import HomeComponent from "./components/HomeComponent";

/*
function App() {
  return (
    <div class="container">
            <header class="header">
                <nav class="header__nav">
                    <a href="#" class="header__link header__link_active">Banners</a>
                    <a href="#" class="header__link">Categories</a>
                </nav>
            </header>
            <main class="main">
                <aside class="sidebar">
                    <header class="sidebar__header">Banners:</header>

                    <section class="sidebar__content">
                        <div class="sidebar__search">
                            <input class="sidebar__search-input" type="text" placeholder="Enter banner name..." />
                            <span class="sidebar__search-icon"></span>
                        </div>
                        <div class="sidebar__menu">
                            <a href="#" class="sidebar__menu-item">Some banner</a>
                            <a href="#" class="sidebar__menu-item">Second banner</a>
                            <a href="#" class="sidebar__menu-item">New test banner</a>
                        </div>
                    </section>

                    <footer class="sidebar__footer">
                        <button class="sidebar__submit-button">Create new Banner</button>
                    </footer>
                </aside>

                <section class="content content_center">
                    Choose an action.
                </section>
            </main>
        </div>
  );
}
*/
class App extends Component {
    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);

        this.state = {
            showAdminBoard: false,
            currentUser: undefined,
        };
    }

    componentDidMount() {
        const user = AuthService.getCurrentUser();

        if (user) {
            this.setState({
                currentUser: user,
                showAdminBoard: user.roles.includes("ROLE_ADMIN"),
            });
        }

        EventBus.on("logout", () => {
            this.logOut();
        });
    }

    componentWillUnmount() {
        EventBus.remove("logout");
    }

    logOut() {
        AuthService.logout();
        this.setState({
            showAdminBoard: false,
            currentUser: undefined,
        });
    }


    render() {
        const { currentUser, showAdminBoard } = this.state;

        return (
            <div className="container">
                <header className="header">
                    <nav className="header__nav">
                        <Link to={"/"} className="header__link">
                        </Link>
                        <div className="header__link mr-auto">
                            {showAdminBoard && (<>
                                <li className="header__link">
                                    <Link to={"/banners"} className="nav-link">
                                        Banners
                                    </Link>
                                </li>
                                <li className="header__link">
                                    <Link to={"/categories"} className="nav-link">
                                        Categories
                                    </Link>
                                </li>
                            </>
                            )
                            }
                        </div>

                        {currentUser ? (
                            <div className="header__link mr-4">
                                {currentUser && (
                                    <li className="header__link">
                                        <Link to={"/bid"} className="header__link">
                                            Bid
                                        </Link>
                                    </li>
                                )
                                }
                                <li className="header__link">
                                    <Link to={"/profile"} className="header__link">
                                        {currentUser.username}
                                    </Link>
                                </li>
                                <li className="header__link">
                                    <a href="/login" className="header__link" onClick={this.logOut}>
                                        LogOut
                                    </a>
                                </li>
                            </div>
                        ) : (
                            <div className="header__link">
                                <li className="header__link">
                                    <Link to={"/login"} className="nav-link">
                                        Login
                                    </Link>
                                </li>

                                <li className="header__link">
                                    <Link to={"/register"} className="nav-link">
                                        Sign Up
                                    </Link>
                                </li>
                            </div>
                        )}
                    </nav>
                </header>
                <main className="main">
                    <section className="container-fluid mt-5">
                        <Switch>
                            {showAdminBoard && <Route path="/admin" component={BoardAdmin} />}
                            {showAdminBoard && <Route path="/banners" component={BannerComponent} />}
                            {showAdminBoard && <Route path="/categories" component={CategoryComponent} />}
                        </Switch>
                
                    
                        <Switch>
                            <Route exact path={"/"} component={HomeComponent} />
                            <Route exact path="/login" component={Login} />
                            <Route exact path="/register" component={Register} />
                            <Route path="/bid" component={BidComponent} />
                            <Route path="/profile" component={ProfileComponent} />
                        </Switch>
                    </section>
                </main>
            </div>
        );
    }
}
export default App;

