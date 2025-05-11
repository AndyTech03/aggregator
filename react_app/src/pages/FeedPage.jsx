import { useContext, useEffect, useState } from "react";
import { UserContext } from "../contexts/UserContext";
import NewsFeed from "../components/NewsFeed";

function FeedPage({...props}) {
	const pageSize = 10
	const { profile } = useContext(UserContext)
	

	return ( 
		<div {...props}>
			<NewsFeed 
				pageSize={pageSize}
				profile={profile}
				/>
		</div>
	);
}

export default FeedPage;