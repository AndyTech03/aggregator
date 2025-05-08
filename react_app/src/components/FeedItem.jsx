import React from "react";
import { Link } from "react-router-dom";
import AddOrDelButton from "./AddOrDelButton";
// import routes from "../routes";

function FeedItem({ filter, item, ...props }) {
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
					<Link to={originUri} onClick={() => alert('click')}>открыть</Link>
				]
			</span>
			<div>
				{item.categories.map((category, key) => 
					<span key={key}>
						<b>#{category}</b>
						<AddOrDelButton item={category} itemsArray={filter.categories}/>
					</span>
				)}
			</div>
			<p dangerouslySetInnerHTML={{__html: item.description}}/>
			<span>
				<b>@{author}</b>
				<AddOrDelButton item={author} itemsArray={filter.authors}/>
				<br />
				{date.toLocaleString()}
				<br />
				{feedUrl}
				<AddOrDelButton item={feedUrl} itemsArray={filter.rssFeeds}/>
			</span>
		</div>
	 );
}

export default FeedItem;