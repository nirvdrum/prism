ProgramNode(0...20)(
  ScopeNode(0...0)([]),
  StatementsNode(0...20)(
    [StringNode(0...10)(
       STRING_BEGIN(0...3)("%q("),
       STRING_CONTENT(3...9)("foobar"),
       STRING_END(9...10)(")"),
       "foobar"
     ),
     StringNode(12...20)(
       STRING_BEGIN(12...13)("'"),
       STRING_CONTENT(13...19)("foobar"),
       STRING_END(19...20)("'"),
       "foobar"
     )]
  )
)