import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import routes from "../routes";
import fetchNews from "../api/fetchNews";
import fetchLikes from "../api/fetchLikes";
import fetchViews from "../api/fetchViews";

function FeedItem({ item, saveFeedState, ...props }) {
	const [news, setNews] = useState({...item})
	const [views, setViews] = useState(null)
	const [likes, setLikes] = useState(null)

	useEffect(() => {
		const newsId = item.id
		fetchNews(newsId)
		.then((found) => {
			setNews(found)
		})
		fetchViews(newsId)
		.then((found) => {
			setViews(found)
		})
		fetchLikes(newsId)
		.then((found) => {
			setLikes(found)
		})
	}, [item])

	const author = news?.author
	const title = news?.title
	const description = news?.description
	const categories = news?.categories
	const rssFeed = news?.rssFeed
	const date = new Date(news?.date)
	const uri = news?.uri
	return ( 
		<div {...props}>
			<hr />
			<span>
				{title}
				[
					<Link to={routes.ARTICLE_PAGE.replace(':id', item.id)} 
					onClick={() => {saveFeedState(); alert('click')}}>читать</Link>|
					<Link to={uri} 
					onClick={() => {saveFeedState(); alert('click')}}>открыть</Link>
				]
			</span>
			<div>
				{likes != null ?
					<>{likes.filter(i => i.sign == 'like').length} likes</> :
					<>loading...</>
				} <br />
				{views != null ? 
					<>{views.length} views</> :
					<>loading...</>
				}
			</div>
			<div>
				{categories?.map((category, key) => 
					<span key={key}>
						<b>#{category}</b>
					</span>
				)}
			</div>
			<span>
				{author != null ?
					<b>@{author.username}</b> :
					<b>loading...</b>
				}
				<br />
				{date != null ?
					<>{date.toLocaleString()}</> :
					<>loading...</>
				}
				<br />
				{rssFeed != null ?
					<>{rssFeed.title}</> :
					<>loading...</>
				}
			</span>
		</div>
	 );
}

export default FeedItem;