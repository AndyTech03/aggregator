import React from "react";
import routes from "../routes";
import { Link, useNavigate } from "react-router-dom";

function Header() {
	const navigate = useNavigate()
	return (
		<nav>
			<ul>
				<li><button onClick={() => navigate(-1)}>Назад</button></li>
				<li><Link to={routes.HOME_PAGE}>Трендовые новости</Link></li>
				<li><Link to={routes.FEED_PAGE}>Лента новостей</Link></li>
				<li><Link to={routes.SEARCH_PAGE}>Поиск</Link></li>
			</ul>
			
		</nav>
	);
}

export default Header;