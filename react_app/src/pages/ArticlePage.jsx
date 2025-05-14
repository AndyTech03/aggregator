import { useEffect, useState } from "react"
import NotFoundPage from "./NotFound"
import { Link, useNavigate, useNavigation, useParams } from "react-router-dom"
import NewsFeed from "../components/NewsFeed"
import { getItemFull } from "../api/rssItemsController"
import AuthorLabel from "../components/AuthorLabel"
import DateLabel from "../components/DateLabel"
import ChanelLabel from "../components/ChanelLabel"
import CategoriesList from "../components/CategoriesList"
import LikesViewsLabel from "../components/LikesViewsLabel"

function ArticlePage({ ...props }) {
	const similarCount = 10;
	const params = useParams()
	const newsId = params?.id
	const navigate = useNavigate()
	const [news, setNews] = useState(null)
	const [state, setState] = useState('loading')

	useEffect(() => {
		getItemFull(newsId)
		.then(found => {
			if (found == null) {
				setState('notFound')
				return
			}
			setNews(found)
			setState('found')
		}).catch(() => {
			setState('error')
		})
	}, [params.id])

	if (state == 'notFound') {
		return <NotFoundPage />
	}
	if (state != 'found') {
		return (
			<div>
				Loading...
			</div>
		)
	}
	const author = news?.author
	const title = news?.title
	const description = news?.description
	const categories = news?.categories?.map(i => i.trim()).filter(i => i.length != 0)
	const rssFeedTitle = news?.feedUrl?.match(/(?<=\:\/\/)(.*?)(?=\/|$)/g)[0]
	const date = new Date(news?.date)
	const uri = news?.uri
	const likesCount = news?.likesCount ?? 0
	const viewsCount = news?.viewsCount ?? 0
	return ( 
		<div {...props}>
			<button onClick={() => navigate(-1)}>Back</button>
			<hr />
			<span>
				{news.title}
				[
					<Link to={uri} onClick={() => {}}>открыть</Link>
				]
			</span>
			{state == 'loading' ?
				<>
					Loading...
				</> :
			state == 'error' ?
				<>
					Error!
				</> :
				<>
					<CategoriesList categories={categories} enableFilter={true} />
					<p dangerouslySetInnerHTML={{__html: news.description}}/>
					<span>
						<ChanelLabel rssFeedTitle={rssFeedTitle} enableFilter={true} />
						<AuthorLabel author={author} enableFilter={true} />
						<DateLabel date={date} />
					</span>
					<LikesViewsLabel likesCount={likesCount} viewsCount={viewsCount} />
				</>
			}
			<hr />
			<br />
			<NewsFeed title="Похожие Новости" pageSize={similarCount} similarId={newsId} />
		</div>
	 );
}

export default ArticlePage;