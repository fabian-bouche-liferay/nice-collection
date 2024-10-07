package com.liferay.samples.fbo.asset.info.custom;

import com.liferay.info.item.renderer.InfoItemRenderer;
import com.liferay.info.item.renderer.InfoItemRendererRegistry;
import com.liferay.info.item.renderer.InfoItemTemplatedRenderer;
import com.liferay.info.list.renderer.DefaultInfoListRendererContext;
import com.liferay.info.list.renderer.InfoListRendererContext;
import com.liferay.info.taglib.list.renderer.BasicInfoListRenderer;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fabian-liferay
 */
public class CustomJournalArticleInfoListRenderer implements BasicInfoListRenderer<JournalArticle>  {

	private String _tagName;
	private long _companyId;
	
	public CustomJournalArticleInfoListRenderer(long companyId, String tagName, InfoItemRendererRegistry infoItemRendererRegistry) {
		_tagName = tagName;
		_companyId = companyId;
		_infoItemRendererRegistry = infoItemRendererRegistry;
	}

	@Override
	public List<InfoItemRenderer<?>> getAvailableInfoItemRenderers() {
		return _infoItemRendererRegistry.getInfoItemRenderers(
			JournalArticle.class.getName());
	}

	@Override
	public String getLabel(Locale locale) {
		return _tagName;
	}
	
	@Override
	public String getKey() {
		return this.getClass().getName() + "#" + _tagName;
	}
	
	@Override
	public void render(
		List<JournalArticle> journalArticles, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {

		render(
			journalArticles,
			new DefaultInfoListRendererContext(
				httpServletRequest, httpServletResponse));
	}

	@Override
	public void render(
			List<JournalArticle> journalArticles,
		InfoListRendererContext infoListRendererContext) {
		
		HttpServletRequest request = infoListRendererContext.getHttpServletRequest();
		HttpServletResponse response = infoListRendererContext.getHttpServletResponse();

		/*
		LayoutStructure layoutStructure = (LayoutStructure) request.getAttribute(LayoutWebKeys.LAYOUT_STRUCTURE);
		System.out.println("Layout Structure " + layoutStructure.getMainLayoutStructureItem().getItemConfigJSONObject().getString("name"));
		*/
		
		String listItemRendererKey =
			infoListRendererContext.getListItemRendererKey();

		if (Validator.isNull(listItemRendererKey)) {
			listItemRendererKey = "com.liferay.journal.web.internal.info.item.renderer.JournalArticleTitleInfoItemRenderer";
		}

		String templateKey = infoListRendererContext.getTemplateKey();

		try {
			renderItems(journalArticles, request, response, listItemRendererKey, templateKey);
		} catch (IOException e) {
			_log.error("Failed to render", e);
		}
		
	}

	private void renderItems(List<JournalArticle> journalArticles, HttpServletRequest request,
			HttpServletResponse response, String listItemRendererKey, String templateKey) throws IOException {
		InfoItemRenderer<JournalArticle> infoItemRenderer = (InfoItemRenderer) _infoItemRendererRegistry.getInfoItemRenderer(listItemRendererKey);
		
		response.getWriter().append("<").append(_tagName).append(">");
		
		for (int i = 0; i < journalArticles.size(); i++) {

			response.getWriter().append("<div class=\"slot-item\" slot=\"item-").append(String.valueOf(i)).append("\" >");

			if(Validator.isNotNull(templateKey) && infoItemRenderer instanceof InfoItemTemplatedRenderer) {
				InfoItemTemplatedRenderer<JournalArticle> infoItemTemplatedRenderer = (InfoItemTemplatedRenderer)infoItemRenderer;
				infoItemTemplatedRenderer.render(journalArticles.get(i), templateKey, request, response);
			} else {
				infoItemRenderer.render(journalArticles.get(i), request, response);
			}

			response.getWriter().append("</div>");
		}
		
		response.getWriter().append("</").append(_tagName).append(">");
	}

	@Override
	public String getListStyle() {
		return _tagName;
	}
	
	protected InfoItemRendererRegistry _infoItemRendererRegistry;

	private static final Log _log = LogFactoryUtil.getLog(
			CustomJournalArticleInfoListRenderer.class);
}