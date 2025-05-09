import React from "react";
import { Link } from "react-router-dom";
import AddOrDelButton from "./AddOrDelButton";
import routes from "../routes";

function FeedItem({ filter, item, saveFeedState, ...props }) {
	const date = new Date(item.date)
	const originUri = item.uri
	const author = item.author
	const feedUrl = item.feedUrl
	return ( 
		<div {...props}>
			<hr />
			<span>
				{item.title}
				[
					<Link to={routes.ARTICLE_PAGE.replace(':id', item.id)} 
					onClick={() => {saveFeedState(); alert('click')}}>читать</Link>|
					<Link to={originUri} 
					onClick={() => {saveFeedState(); alert('click')}}>открыть</Link>
				]
			</span>
			<div>
				{item.categories.map((category, key) => 
					<span key={key}>
						<b>#{category}</b>
						{filter &&
							<AddOrDelButton item={category} itemsArray={filter.categories}/>
						}
					</span>
				)}
			</div>
			<span>
				<b>@{author}</b>
				{filter &&
					<AddOrDelButton item={author} itemsArray={filter.authors}/>
				}
				<br />
				{date.toLocaleString()}
				<br />
				{feedUrl}
				{filter &&
					<AddOrDelButton item={feedUrl} itemsArray={filter.rssFeeds}/>
				}
			</span>
		</div>
	 );
}

export default FeedItem;