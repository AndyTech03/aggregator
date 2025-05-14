import { useEffect, useState } from "react"
import fetchArticle from "../api/fetchArticle"
import NotFoundPage from "./NotFound"
import { Link, useNavigate, useNavigation, useParams } from "react-router-dom"
import AddOrDelButton from "../components/AddOrDelButton"
import useArray from "../hooks/useArray"
import NewsFeed from "../components/NewsFeed"

function ArticlePage({ ...props }) {
	const similarCount = 10;
	const params = useParams()
	const newsId = params?.id
	const navigate = useNavigate()
	const [item, setItem] = useState(null)
	const [state, setState] = useState('loading')

	const categoriesFilter = useArray({
		cookiesName: 'categoriesFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	const authorsFilter = useArray({
		cookiesName: 'authorsFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	const rssFeedsFilter = useArray({
		cookiesName: 'rssFeedsFilter',
		defaultValue: [],
		useStorage: true,
		global: true,
	})
	
	useEffect(() => {
		fetchArticle(newsId)
		.then(found => {
			if (found?.news == null) {
				setState('notFound')
				return
			}
			setItem(found)
			setState('found')
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
	const news = item.news;
	const date = new Date(news.date)
	const originUri = news.uri
	const author = news.author
	const rssFeed = news.rssFeed
	return ( 
		<div {...props}>
			<button onClick={() => navigate(-1)}>Back</button>
			<hr />
			<span>
				{news.title}
				[
					<Link to={originUri} onClick={() => alert('click')}>открыть</Link>
				]
			</span>
			<div>
				{news.categories.map((category, key) => 
					<span key={key}>
						<b>#{category.title}</b>
						{categoriesFilter != null &&
							<AddOrDelButton item={category.id} itemsArray={categoriesFilter}/>
						}
					</span>
				)}
			</div>
			<p dangerouslySetInnerHTML={{__html: news.description}}/>
			<span>
				<b>@{author.username}</b>
				{authorsFilter != null &&
					<AddOrDelButton item={author.id} itemsArray={authorsFilter}/>
				}
				<br />
				{date.toLocaleString()}
				<br />
				{rssFeed.title}
				{rssFeedsFilter != null &&
					<AddOrDelButton item={rssFeed.id} itemsArray={rssFeedsFilter}/>
				}
			</span>
			<hr />
			<br />
			<NewsFeed title="Похожие Новости" pageSize={similarCount} similarId={newsId} />
		</div>
	 );
}

export default ArticlePage;