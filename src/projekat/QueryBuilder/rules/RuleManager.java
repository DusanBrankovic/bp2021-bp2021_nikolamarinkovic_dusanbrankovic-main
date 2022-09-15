package projekat.QueryBuilder.rules;

import lombok.Getter;

@Getter
public class RuleManager {

    private ParameterNumberRule parameterNumberRule;
    private ParameterTypeRule parameterTypeRule;
    private WhereInAndParametersRule whereInAndParametersRule;
    private JoinAndOnRule joinAndOnRule;
    private AggrFuncInSelectRule aggrFuncInSelectRule;
    private AggrFuncAliasRule aggrFuncAliasRule;
    private HavingParametersRule havingParametersRule;

    public RuleManager(){
        initialiseRules();
    }

    public void initialiseRules(){

        parameterNumberRule = new ParameterNumberRule();
        parameterTypeRule = new ParameterTypeRule();
        whereInAndParametersRule = new WhereInAndParametersRule();
        joinAndOnRule = new JoinAndOnRule();
        aggrFuncInSelectRule = new AggrFuncInSelectRule();
        aggrFuncAliasRule = new AggrFuncAliasRule();
        havingParametersRule = new HavingParametersRule();
    }
}
