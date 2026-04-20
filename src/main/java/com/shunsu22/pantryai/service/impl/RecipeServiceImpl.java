package com.shunsu22.pantryai.service.impl;

import com.shunsu22.pantryai.entity.Recipe;
import com.shunsu22.pantryai.entity.User;
import com.shunsu22.pantryai.repository.RecipeRepository;
import com.shunsu22.pantryai.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

   /* @Override
    public String suggestRecipe(List<String> ingredientNames) {
        String ingredients = String.join("、", ingredientNames);
        String prompt = String.format(
                """
                        冷蔵庫に以下の食材があります：%s
                        これらの食材を使って作れる料理を1つ提案してください。
                        レシピ名、必要な材料、調理手順を日本語で教えてください。""",
                ingredients
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(message)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OpenAiResponse> response = restTemplate.postForEntity(
                OPENAI_URL, request, OpenAiResponse.class);
        return Objects.requireNonNull(response.getBody()).choices().getFirst().message().content();
    }*/

    /**
     * TODO :AIレシピ提案は一旦後回し、実装が落ち着いたら課金して動作確認
     */
   @Override
   public String suggestRecipe(List<String> ingredientNames) {
       String ingredients = String.join("、", ingredientNames);
       // TODO: OpenAI API課金後に実装を戻す
       return String.format(
               """
                       【モックレスポンス】
                       
                       食材：%s
                       
                       レシピ名：野菜炒め
                       
                       材料：
                       - 登録食材を適量
                       - 塩コショウ
                       - サラダ油
                       
                       手順：
                       1. 食材を適当な大きさに切る
                       2. フライパンに油を熱する
                       3. 食材を炒める
                       4. 塩コショウで味を整える
                       5. 完成！""",
               ingredients
       );
   }

    @Override
    @Transactional
    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> findByUser(User user) {
        return recipeRepository.findByUserAndIsDeletedFalse(user);
    }

    @Override
    public Recipe findById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("レシピが見つかりません: " + id));
    }

    @Override
    @Transactional
    public void toggleFavorite(Long id) {
        Recipe recipe = findById(id);
        recipe.setIsFavorite(!recipe.getIsFavorite());
        recipeRepository.save(recipe);
    }
}