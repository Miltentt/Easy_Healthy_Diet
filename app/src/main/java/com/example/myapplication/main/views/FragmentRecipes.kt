package com.example.myapplication.main.views

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.di.ViewModelsProviderFactory
import com.example.myapplication.main.adapters.RecipesListAdapter
import com.example.myapplication.main.viewmodels.Meal_Recipe_SharedViewModel
import com.example.myapplication.models.edamamResponse.EdamamResponse
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCradviewMealsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FragmentRecipes @Inject constructor() : DaggerFragment() {
    lateinit var meal_viewmodel : Meal_Recipe_SharedViewModel
    @Inject
    lateinit var  viewmodelprovider : ViewModelsProviderFactory
    private  var recipe_adapter = RecipesListAdapter{ recipe,image ->onClick(recipe,image)}
    private lateinit var binding : FragmentCradviewMealsBinding
    private var mealtype : String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        meal_viewmodel =
            ViewModelProviders.of(requireActivity(), viewmodelprovider)[Meal_Recipe_SharedViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cradview_meals,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealtype=savedInstanceState?.getString("mealType")
        initRecycler()
        initLiveData()
    }

    private fun initRecycler() {
        binding.recipeRecycler.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = recipe_adapter
        }
    }
 private fun initLiveData()
 {
     meal_viewmodel.returnRecipeLiveData().observe({lifecycle}, {
         recipe_adapter.submitList(it)
             binding.recipeRecycler.visibility=View.VISIBLE
         binding.progress.visibility=View.GONE
         })
 }

    private fun onClick(recipe : EdamamResponse.Hit.Recipe, image: ImageView)
    {
        val bundle = Bundle()
        bundle.putParcelable("recipe",recipe)
        bundle.putString("mealType",mealtype)
        val fragmentRecipe = FragmentRecipe()
        fragmentRecipe.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment,fragmentRecipe)
            .addSharedElement(image,recipe.label)
            .addToBackStack(null)
            .commit()

    }

}