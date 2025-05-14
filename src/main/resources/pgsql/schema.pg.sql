CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE INDEX IF NOT EXISTS idx_rss_items_title_trgm
    ON rss_items USING GIN (title gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_rss_items_description_trgm
    ON rss_items USING GIN (description_flat gin_trgm_ops);

CREATE INDEX IF NOT EXISTS rss_items_categories_trgm_idx
    ON rss_items USING GIN (categories_flat gin_trgm_ops);

CREATE INDEX IF NOT EXISTS rss_items_author_trgm_idx
    ON rss_items USING GIN (author gin_trgm_ops);

CREATE INDEX IF NOT EXISTS rss_items_feed_url_trgm_idx
    ON rss_items USING GIN (feed_url gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_rss_items_date
    ON rss_items USING BRIN (COALESCE(published_date, parsed_date));