import { useContext } from "react";
import { UserContext } from "../contexts/UserContext";
import TopNewsList from "../components/TopNewsList";
import Header from "../components/Header";

function HomePage({ ...props }) {
	const user = useContext(UserContext)
	return ( 
		<div {...props}>
			<Header/>
			{user.userId} <br />
			{user.profile}
			<TopNewsList />
		</div>
	);
}

export default HomePage;