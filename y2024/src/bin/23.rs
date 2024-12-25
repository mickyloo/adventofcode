use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use counter::Counter;
use itertools::Itertools;
use std::collections::HashSet;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "23"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(mut reader: R) -> Result<usize> {
        let networks = networks(&mut reader);

        let answer = networks.into_iter()
            .filter(|combo| {
                combo[0].starts_with("t") || combo[1].starts_with("t") || combo[2].starts_with("t")
            })
            .collect_vec()
            .len();

        Ok(answer)
    }
    fn networks<R: BufRead>(reader: R) -> Vec<Vec<String>> {
        let mut computers: HashSet<String> = HashSet::new();
        let mut connections: HashSet<(String, String)> = HashSet::new();

        reader.lines().for_each(|line| {
            let binding = line.unwrap();
            let (left, right) = binding.split_once("-").unwrap();
            computers.insert(left.to_string());
            computers.insert(right.to_string());
            connections.insert((left.to_string(), right.to_string()));
            connections.insert((right.to_string(), left.to_string()));
        });

        let combos = computers
            .iter()
            .combinations(3)
            .filter(|combo| {
                let ab = (combo[0].clone(), combo[1].clone());
                let bc = (combo[1].clone(), combo[2].clone());
                let ac = (combo[0].clone(), combo[2].clone());

                connections.contains(&ab) && connections.contains(&bc) && connections.contains(&ac)
            })
            .map(|combo| combo.iter().map(|&x| x.to_owned()).collect_vec())
            .collect_vec();

        combos
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(7, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
