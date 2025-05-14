import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import routes from "../routes";
import { getItem } from "../api/rssItemsController";
import ChanelLabel from "./ChanelLabel";
import AuthorLabel from "./AuthorLabel";
import DateLabel from "./DateLabel";
import CategoriesList from "./CategoriesList";
import LikesViewsLabel from "./LikesViewsLabel";

function FeedItem({ item, saveFeedState, ...props }) {
	const [news, setNews] = useState({...item})
	const [loading, setLoading] = useState('loading')

	useEffect(() => {
		const newsId = item.id
		getItem(newsId)
		.then((found) => {
			setNews(found)
			setLoading('complied')
		}).catch(() => {
			setLoading('error')
		})
	}, [item])

	const author = news?.author
	const title = news?.title ?? item.title
	const categories = news?.categories?.map(i => i.trim()).filter(i => i.length != 0)
	const rssFeedTitle = news?.feedUrl?.match(/(?<=\:\/\/)(.*?)(?=\/|$)/g)[0]
	const date = new Date(news?.date)
	const uri = news?.uri
	const likesCount = news?.likesCount ?? 0
	const viewsCount = news?.viewsCount ?? 0
	return ( 
		<div {...props}>
			<hr />
			<span>
				{title}
				[
					<Link to={routes.ARTICLE_PAGE.replace(':id', item.id)} 
					onClick={() => {saveFeedState(); }}>читать</Link>|
					<Link to={uri} 
					onClick={() => {saveFeedState(); }}>открыть</Link>
				]
			</span>
			{loading == 'loading' ? 
				<>
					Loading...
				</> :
			loading == 'error' ?
				<>
					Error!
				</> :
				<>
					<CategoriesList categories={categories} />
					<span>
						<ChanelLabel rssFeedTitle={rssFeedTitle} />
						<AuthorLabel author={author} />
						<DateLabel date={date} />
					</span>
					<LikesViewsLabel likesCount={likesCount} viewsCount={viewsCount} />
				</>
			}
			
		</div>
	 );
}

export default FeedItem;