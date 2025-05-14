import { newsArray, likesArray, viewsArray } from "./faker/news";

export default async function fetchFeed(offset, count, similarId) {
	return newsArray
	.filter(i => i.id != similarId)
	.map((news) => {
		return {
			id: news.id,
			title: news.title,
			likes: likesArray.filter(i => i.newsId == news.id).length,
			views: viewsArray.filter(i => i.newsId == news.id).length,
		}
	})
	.sort((a, b) => b.views - a.views || b.likes - a.likes)
	.slice(offset, offset+count)
	.map((news) => {
		return {
			id: news.id,
			title: news.title,
		}
	})
}
