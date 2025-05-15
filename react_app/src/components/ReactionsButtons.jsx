import { useCallback, useEffect, useState } from "react";
import { getReaction, setReaction } from "../api/socialController";

const ReactionsType = {
	LIKE: 'LIKE',
	DISLIKE: 'DISLIKE',
	NONE: 'NONE',
}

function ReactionsButtons({ userId, newsId, ...props }) {
	const [type, setType] = useState(ReactionsType.NONE)
	const [loading, setLoading] = useState('loading')
	const [uploading, setUploading] = useState('awaiting')

	const loadReaction = useCallback(() => {
		setLoading('loading')
		getReaction(userId, newsId)
		.then((found) => {
			if (found != null)
				setType(found)
			setLoading('complied')
		})
		.catch(() => {
			setLoading('error')
		})
	}, [userId, newsId])

	const uploadReaction = useCallback((reaction) => {
		if (uploading != 'awaiting')
			return
		setUploading('uploading')
		setReaction(userId, newsId, reaction)
		.then(() => {
			setType(reaction)
		})
		.catch(() => {
			alert('Что то пошло не так...')
		})
		.finally(() => {
			setUploading('awaiting')
		})
	}, [userId, newsId])

	useEffect(() => {
		setType(ReactionsType.NONE)
		loadReaction()
	}, [userId, newsId, newsId])

	return ( 
		<div {...props}>
			{loading == 'loading' ?
				<>
					Loading...
				</> :
			loading == 'error' ?
				<>
					Error!	
				</> :
				<>
					{type}
					<br />
					{type == ReactionsType.NONE || type == ReactionsType.DISLIKE ? 
						<button onClick={() => uploadReaction(ReactionsType.LIKE)}>Like</button> :
						<button onClick={() => uploadReaction(ReactionsType.NONE)}>Clear like</button>
					}
					{type == ReactionsType.NONE || type == ReactionsType.LIKE ? 
						<button onClick={() => uploadReaction(ReactionsType.DISLIKE)}>Dislike</button> :
						<button onClick={() => uploadReaction(ReactionsType.NONE)}>Clear dislike</button>
					}
				</>
			}
			
		</div>
	);
}

export default ReactionsButtons;