import { apiHost } from "../routes";

const host = `${apiHost}/rss_items`
export async function getLatest(offset, limit) {
	return fetch(`${host}/getLatest`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			offset, 
			limit,
		})
	}).then((response) => response.json())
}

export async function getItem(newsId) {
	return fetch(`${host}/getItem`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			id: newsId, 
		})
	}).then((response) => response.json())
}

export async function getItemFull(newsId) {
	return fetch(`${host}/getItemFull`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			id: newsId, 
		})
	}).then((response) => response.json())
}

export async function getSimilar(similarId, offset, limit) {
	return fetch(`${host}/getSimilar`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			similarId: similarId,
			offset: offset,
			limit: limit
		})
	}).then((response) => response.json())
}

export async function search(query, categories, offset, limit) {
	return fetch(`${host}/search`, {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
		},
		body: JSON.stringify({
			query,
			categories,
			offset, 
			limit,
		})
	}).then((response) => response.json())
}